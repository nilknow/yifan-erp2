import React from "react";
import AddAccountModalButton from "@/lib/admin/addAccountModalButton";
import AddRoleModalButton from "@/lib/admin/addRoleModalButton";

export default function Index() {

  return (
    <>
     <div className={"flex"}>
       <AddRoleModalButton/>
       <AddAccountModalButton/>
     </div>
    </>
  )
}
