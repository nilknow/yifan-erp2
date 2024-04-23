import TopNavBar from "@/lib/TopNavBar";
import {Suspense} from "react";

export default function Layout({children}: { children: React.ReactNode }) {
  return (
    <Suspense>
      <div>
        {children}
      </div>
    </Suspense>

  )
}
