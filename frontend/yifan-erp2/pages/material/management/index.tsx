import React from "react";
import BatchAddModalButton from "@/lib/material/batchAddModalButton";
import MaterialTable from "@/lib/material/materialTable";
import MaterialAddModalButton from "@/lib/material/addModalButton";

export default function Index() {
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
