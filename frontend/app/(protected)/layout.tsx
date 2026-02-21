"use client";

import { ReactNode } from "react";
import { useAuth } from "@/hooks/useAuth";

export default function ProtectedLayout({
  children,
}: {
  children: ReactNode;
}) {
  const auth = useAuth({ required: true });

  if (auth.status === "loading") {
    return null;
  }

  return <>{children}</>;
}
