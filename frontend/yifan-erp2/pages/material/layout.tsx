import TopNav from "@/lib/topNav";

export default function Layout({children}: { children: React.ReactNode }) {
  return (
    <div>
      <TopNav></TopNav>
      <div>
        {children}
      </div>
    </div>
  );
}
