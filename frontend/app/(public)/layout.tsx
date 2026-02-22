"use client";

import { ReactNode } from "react";
import { useAuth } from "@/hooks/useAuth";

export default function PublicLayout({
  children,
}: {
  children: ReactNode;
}) {
  const auth = useAuth({ required: false });

  if (auth.status === "loading") {
    return null;
  }

  return <>{children}</>;
}
