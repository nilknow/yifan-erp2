'use client'
import React from "react";
import AddAccountModalButton from "@/app/lib/admin/addAccountModalButton";
import AddRoleModalButton from "@/app/lib/admin/addRoleModalButton";

export default function Page() {

  return (
    <>
     <div className={"flex"}>
       <AddRoleModalButton/>
       <AddAccountModalButton/>
     </div>
    </>
  )
}
