import React from "react";
import BatchAddModalButton from "@/app/lib/material/batchAddModalButton";
import MaterialTable from "@/app/lib/material/materialTable";
import MaterialAddModalButton from "@/app/lib/material/addModalButton";

export default function Page() {
  return (
    <div className={"mx-4"}>
      <MaterialTable>
        {
          <>
            <MaterialAddModalButton></MaterialAddModalButton>
            <BatchAddModalButton></BatchAddModalButton>
          </>
        }
      </MaterialTable>
    </div>
  )
}
