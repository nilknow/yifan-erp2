import TopNavBar from "@/lib/TopNavBar";

export default function Layout({children}: { children: React.ReactNode }) {
  return (
    <div>
      <TopNavBar></TopNavBar>
      {children}
    </div>
  );
}
