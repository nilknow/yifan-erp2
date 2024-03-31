import NavBox from "@/app/lib/index/NavBox";

export default function Home() {
  return (
    <main>
      <a href={"/"}>
        <img src={"/66_long.png"} className={"w-8 h-8 m-5"}/>
      </a>
      <NavBox title={"产品管理"} links={[
        {path: "/product/page/create", label: "添加产品"},
        {path: "/product/plan", label: "生产计划（开发中，暂时不可用）"},
        {path: "/product/list", label: "产品管理"},
        {path: "/product/list", label: "产品出库"},
        {path: "/product/list", label: "产品售后"},
        {path: "/bom", label: "BOM管理"}
      ]}></NavBox>
      <NavBox title={"物料管理"} links={[
        {path: "/material", label: "查看"},
        {path: "/material/buy", label: "原材料采购（开发中）"},
        {path: "/material/create", label: "创建物料"},
        {path: "/material/add", label: "物料入库"},
      ]}></NavBox>
      <NavBox title={"BOM管理"} links={[
        {path: "/bom", label: "BOM管理"}
      ]}></NavBox>
      <NavBox title={"预警管理"} links={[
        {path: "/alert/list", label: "预警管理"}
      ]}></NavBox>
      <NavBox title={"66进销存"} links={[
        {path: "/documentation/page", label: "产品文档"},
        {path: "/release/page", label: "更新记录"},
        {path: "/suggestion/page", label: "更新建议"},
        {path: "/bug-report/page", label: "点这里可以提交bug！！！"}
      ]}></NavBox>
    </main>
  );
}
