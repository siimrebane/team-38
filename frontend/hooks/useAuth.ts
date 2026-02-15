"use client";

import { useEffect, useState } from "react";
import { usePathname, useRouter } from "next/navigation";

export interface AuthUser {
  id: number | string;
  email: string;
  name?: string;
  organizationId?: number;
  needsOnboarding: boolean;
  role?: string;
}

type AuthState =
  | { status: "loading" }
  | { status: "authenticated"; user: AuthUser }
  | { status: "unauthenticated" };

/**
 * Client-side auth hook. Fetches /api/account and redirects based on auth state.
 *
 * - On protected pages: redirects to /login if not authenticated
 * - On public pages (like /login): redirects to /dashboard if already authenticated
 * - Handles onboarding redirects
 */
export function useAuth({ required = true }: { required?: boolean } = {}) {
  const [state, setState] = useState<AuthState>({ status: "loading" });
  const router = useRouter();
  const pathname = usePathname();

  useEffect(() => {
    let cancelled = false;

    (async () => {
      try {
        const res = await fetch("/api/account", {
          credentials: "include",
          cache: "no-store",
        });

        if (cancelled) return;

        if (res.status === 401 || !res.ok) {
          setState({ status: "unauthenticated" });

          if (required) {
            router.replace("/login/");
          }
          return;
        }

        const user: AuthUser = await res.json();
        setState({ status: "authenticated", user });

        // Redirect logged-in users away from login page
        if (!required && pathname === "/login/") {
          router.replace(user.needsOnboarding ? "/onboarding/" : "/dashboard/");
          return;
        }

        // Redirect to onboarding if needed
        if (
          required &&
          user.needsOnboarding &&
          pathname !== "/onboarding/"
        ) {
          router.replace("/onboarding/");
          return;
        }

        // Redirect away from onboarding if already onboarded
        if (pathname === "/onboarding/" && !user.needsOnboarding) {
          router.replace("/dashboard/");
        }
      } catch {
        if (cancelled) return;
        setState({ status: "unauthenticated" });
        if (required) {
          router.replace("/login/");
        }
      }
    })();

    return () => {
      cancelled = true;
    };
  }, [required, router, pathname]);

  return state;
}
