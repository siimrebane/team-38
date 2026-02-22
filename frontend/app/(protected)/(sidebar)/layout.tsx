"use client";

import { ReactNode } from "react";
import { usePathname, useSearchParams } from "next/navigation";
import {
  SidebarProvider,
  SidebarTrigger,
} from "@/components/ui/sidebar";
import { AppSidebar } from "@/components/sidebar";

export default function ProtectedLayout({ children }: { children: ReactNode }) {
  const pathname = usePathname();
  const searchParams = useSearchParams();
  // Hide sidebar on POS station view (e.g., /pos?station=123)
  const isPOSStationView = (pathname === "/pos" || pathname === "/pos/") && searchParams.has("station");

  return (
    <SidebarProvider>
      {!isPOSStationView && <AppSidebar />}
        <main className="w-full">
            {!isPOSStationView && <SidebarTrigger className="ml-4 mt-4" />}
            {children}
        </main>
    </SidebarProvider>
  );
}
