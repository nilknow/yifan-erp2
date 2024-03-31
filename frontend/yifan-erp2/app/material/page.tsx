import LinkButton from "@/app/lib/LinkButton";
import Form from "@/app/lib/material/Form";

export default function Page() {
  return (
    <div>
      {/*<LinkButton urlLink={"/material/page/add"} text={'添加物料'}></LinkButton>*/}
      {/*<form id="full-update-form" action="/material/excel/add" method="post" encType="multipart/form-data">*/}
      {/*  <input id="full-update-file" type="file" name="file" accept=".xlsx, .xls"/>*/}
      {/*  <button type="submit">全量更新物料</button>*/}
      {/*</form>*/}
      {/*<LinkButton urlLink={"/material/excel/template"} text={'下载全量更新物料模板'}></LinkButton>*/}
      {/*<LinkButton urlLink={"/material/excel/download"} text={'下载物料库存excel文档'}></LinkButton>*/}
      {/*<button id="remove-all-popup-btn" className="warn">重置（清空所有物料库存）</button>*/}
      <Form></Form>
    </div>
  )
}
