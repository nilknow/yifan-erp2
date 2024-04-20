'use client'
import React from "react";
import AddAccountModalButton from "@/app/lib/admin/addAccountModalButton";
import AddRoleModalButton from "@/app/lib/admin/addRoleModalButton";
import TopNavBar from "@/app/lib/TopNavBar";

export default function Page() {

  return (
    <>
      <TopNavBar/>
      <div className={"mx-4 flex"}>
        <AddRoleModalButton/>
        <AddAccountModalButton/>
      </div>
    </>
  )
}
