'use client'
import NavBox from "@/app/lib/index/NavBox";
import Image from 'next/image';

export default function Home() {
  return (
    <main>
      <div className={"flex items-center"}>
        <a href={"/"}>
          <img src={"/66_long.png"} className={"w-8 h-8 m-5"} loading={"eager"}/>
        </a>
        <a href={"/search"}>
          <Image alt="Search Icon" src={"/search.svg"}  width={24} height={24} />
        </a>
      </div>
      <NavBox title={"成品管理"} links={[
        {path: "/product", label: "查看"},
        // {path: "/product/create", label: "添加成品"},
        // {path: "/product/plan", label: "生产计划（开发中，暂时不可用）"},
        {path: "/product/management", label: "成品管理"},
        {path: "/product/order", label: "订单管理"},
        {path: "/product/order/history", label: "订单历史记录"},
        // {path: "/product/list", label: "成品出库"},
        // {path: "/product/list", label: "成品售后"},
        {path: "/bom", label: "BOM管理"}
      ]}></NavBox>
      <NavBox title={"物料管理"} links={[
        {path: "/material", label: "查看"},
        // todo
        // {path: "/material/buy", label: "原材料采购（开发中）"},
        // {path: "/material/create", label: "创建物料"},
        {path: "/material/management", label: "物料管理"},
        {path: "/material/batch", label: "批量处理"},
        {path: "/material/history", label: "历史记录"},
      ]}></NavBox>
      <NavBox title={"BOM管理"} links={[
        {path: "/bom", label: "BOM管理"}
      ]}></NavBox>
      <NavBox title={"预警管理"} links={[
        {path: "/alert", label: "预警消息"},
        // {path: "/alert/management", label: "预警管理（开发中)"}
      ]}></NavBox>
      <NavBox title={"66进销存"} links={[
        // {path: "/documentation", label: "成品文档"},
        // {path: "/release", label: "更新记录"},
        // {path: "/admin", label: "系统管理员"},
        {path: "/suggestion", label: "更新建议"},
        {path: "/bug", label: "点这里可以提交bug！！！"}
      ]}></NavBox>
    </main>
  );
}
