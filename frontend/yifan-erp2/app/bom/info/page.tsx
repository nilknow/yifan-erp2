'use client'
import {
  Table,
  TableBody,
  TableCell,
  TableColumn,
  TableHeader,
  TableRow,
  Tooltip,
} from "@nextui-org/react";
import React, {useEffect, useState} from "react";
import {SearchIcon} from "@nextui-org/shared-icons";
import {Input} from "@nextui-org/input";
import Material from "@/app/dto/material";
import {useSearchParams} from "next/navigation";
import ProductMaterialRel from "@/app/dto/productMaterialRel";
import Res from "@/app/dto/res";
import AddMaterialModalButton from "@/app/lib/bom/info/addMaterialModalButton";
import DeleteModalDeleteIcon from "@/app/lib/bom/info/deleteMaterialModalButton";
import myFetch from "@/app/myFetch";


export default function Page() {
  const searchParams = useSearchParams();
  const [sortedProductMaterialRels, setSortedProductMaterialRels] = useState<ProductMaterialRel[]>([]);
  const [materialTypes, setMaterialTypes] = useState<string[]>([]);
  const [materials, setMaterials] = useState<Material[]>([]);
  const productId = searchParams.get('productId');

  useEffect(() => {
    myFetch(`/api/bom/info?productId=${productId}`)
      .then((res) => res.json())
      .then((data) => {
        setSortedProductMaterialRels(data)
      })
  }, [])

  useEffect(() => {
    myFetch('/api/material/list')
      .then((res) => res.json())
      .then((data: Res<Material[]>) => {
        if (data.successCode !== "success") {
          alert(data.msg)
          return
        }
        setMaterials(data.body)
        setMaterialTypes(Array.from(new Set(data.body.map(material => material.category))))
      })
  }, [])

  return (
    <div className={"mx-4"}>
      <h1 className={"bg-neutral-100"}></h1>
      <div className="flex justify-between gap-3 items-end">
        <Input
          isClearable
          className="w-full sm:max-w-[44%]"
          placeholder="Search by name..."
          startContent={<SearchIcon/>}
        />
        <div className="flex gap-3">
          <AddMaterialModalButton></AddMaterialModalButton>
        </div>
      </div>
      <br/>
      <Table>
        <TableHeader>
          <TableColumn>物料编号</TableColumn>
          <TableColumn>物料分类</TableColumn>
          <TableColumn>物料名</TableColumn>
          <TableColumn>物料数量</TableColumn>
          <TableColumn>操作</TableColumn>
        </TableHeader>
        <TableBody emptyContent={"该成品目前没有BOM，请点击右上角\"添加物料\"按钮添加BOM需要的物料"}>
          {sortedProductMaterialRels.map((materialInfo) => (
            <TableRow key={materialInfo.material.id}>
              <TableCell>{materialInfo.material.serialNum}</TableCell>
              <TableCell>{materialInfo.material.category}</TableCell>
              <TableCell>{materialInfo.material.name}</TableCell>
              <TableCell>{materialInfo.materialCount}</TableCell>
              <TableCell>
                <div className="relative flex items-center gap-2">
                  <Tooltip color={"danger"} content="删除物料">
                    <span className="text-lg text-danger cursor-pointer active:opacity-50">
                      <DeleteModalDeleteIcon {...materialInfo}></DeleteModalDeleteIcon>
                    </span>
                  </Tooltip>
                </div>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  )
}
