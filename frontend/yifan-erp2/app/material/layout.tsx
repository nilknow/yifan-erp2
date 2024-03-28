import TopNavBar from "@/app/lib/index/TopNavBar";

export default function Layout({children}: { children: React.ReactNode }) {
  return (
    <div>
      <TopNavBar></TopNavBar>
      <div>
        {children}
      </div>
    </div>
  );
}
