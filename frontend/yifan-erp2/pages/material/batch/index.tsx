import NavBox from "@/lib/index/NavBox";

export default function Index() {
  return (
    <>
      <NavBox title={"物料批量处理"} links={[
        // {path: "/api/material/excel/template", label: "模板下载"},
        {path: "/api/material/excel/export", label: "导出物料数据"},
        {path: "/material/batch/add", label: "添加"},
        {path: "/material/batch/update", label: "更新"},
      ]}></NavBox>
    </>
  )
}
