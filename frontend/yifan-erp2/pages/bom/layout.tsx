import TopNavBar from "@/lib/TopNavBar";

export default function Layout({children}: { children: React.ReactNode }) {
  return (
    <div>
      <TopNavBar/>
      <div>
        {children}
      </div>
    </div>
  )
}
