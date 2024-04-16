'use client'
import {
  Button,
  Dropdown,
  DropdownItem,
  DropdownMenu,
  DropdownTrigger, getKeyValue, Select, SelectItem,
  Table,
  TableBody,
  TableCell,
  TableColumn,
  TableHeader,
  TableRow,
  Tooltip
} from "@nextui-org/react";
import ModifyModalEditIcon from "@/app/lib/material/modifyModalButton";
import DeleteModalDeleteIcon from "@/app/lib/material/deleteModalDeleteIcon";
import React, {useEffect, useState} from "react";
import Material from "@/app/dto/material";
import Res from "@/app/dto/res";
import {Input} from "@nextui-org/input";
import {SearchIcon} from "@nextui-org/shared-icons";
import myFetch from "@/app/myFetch";
import {AsyncListData, useAsyncList} from "@react-stately/data";

export default function MaterialTable({children}: { children: React.ReactNode }) {
  const [sortedMaterials, setSortedMaterials] = useState<Material[]>([]);
  const [filteredSortedMaterials, setFilteredSortedMaterials] = useState<Material[]>([]);
  const [materialTypes, setMaterialTypes] = React.useState<string[]>([]);
  const [selectedType, setSelectedType] = React.useState<string>('');
  useEffect(() => {
    myFetch('/api/material/list')
      .then((res) => res.json())
      .then((data: Res<Material[]>) => {
        if ("success" === data.successCode) {
          setSortedMaterials(data.body)
          setMaterialTypes(Array.from(new Set(data.body.map(material => material.category))))
        } else {
          alert("查询失败，请稍后重试")
        }
      })
  }, [])

  useEffect(() => {
    if (selectedType !== '') {
      setFilteredSortedMaterials(sortedMaterials.filter((m: Material) => m.category === selectedType));
    } else {
      setFilteredSortedMaterials(sortedMaterials)
    }
  }, [selectedType, sortedMaterials])

  let list:AsyncListData<Material> = useAsyncList({
    async load({signal, filterText}) {
      let res = await myFetch(`/api/material/list?name=${filterText}`)
      let json: Res<Material[]> = await res.json();
      return {
        items: json.body,
      };
    },

    async sort({items, sortDescriptor}) {
      return {
        items: items.sort((a, b) => {
          // @ts-ignore
          let first = a[sortDescriptor.column];
          // @ts-ignore
          let second = b[sortDescriptor.column];
          let cmp = (parseInt(first) || first) < (parseInt(second) || second) ? -1 : 1;

          if (sortDescriptor.direction === "descending") {
            cmp *= -1;
          }

          return cmp;
        }),
      };
    },
  });


  // @ts-ignore
  async function search(e: React.FormEvent<HTMLFormElement>, list) {
    e.preventDefault()
    const formData = new FormData(e.currentTarget)
    const name = formData.get('name')
    if (name === null) {
      list.setFilterText("")
      return
    } else {
      list.setFilterText(name)
    }
  }

  // @ts-ignore
  return (
    <>
      <div>
        <form>
          <div className="flex justify-between gap-3 items-end">
            <Input
              type="text" name="name" size={"sm"}
              onKeyUp={(e) => list.setFilterText(e.currentTarget.value)}
              isClearable
              radius="lg"
              classNames={{
                label: "text-black/50 dark:text-white/90",
                input: [
                  "bg-transparent",
                  "text-black/90 dark:text-white/90",
                  "placeholder:text-default-700/50 dark:placeholder:text-white/60",
                ],
                innerWrapper: "bg-transparent",
                inputWrapper: [
                  "shadow-xl",
                  "bg-default-200/50",
                  "dark:bg-default/60",
                  "backdrop-blur-xl",
                  "backdrop-saturate-200",
                  "hover:bg-default-200/70",
                  "dark:hover:bg-default/70",
                  "group-data-[focused=true]:bg-default-200/50",
                  "dark:group-data-[focused=true]:bg-default/60",
                  "!cursor-text",
                ],
              }}
              placeholder="输入物料名称..."
              startContent={
                <SearchIcon
                  className="text-black/50 mb-0.5 dark:text-white/90 text-slate-400 pointer-events-none flex-shrink-0"/>
              }
            />
            <div className="flex gap-3">
              {children}
              <Button size={"sm"} radius={"full"} color="default"
                      onClick={() => window.location.href = '/api/material/excel/export'}>批量导出</Button>
            </div>
          </div>
        </form>
      </div>
      <br/>
      <Table
        removeWrapper isStriped aria-label="material list table"
        sortDescriptor={list.sortDescriptor}
        onSortChange={list.sort}
      >
        <TableHeader>
          <TableColumn allowsSorting key={"serialNum"}>编号</TableColumn>
          <TableColumn allowsSorting key={"name"}>物料名</TableColumn>
          <TableColumn allowsSorting key={"category"}>分类</TableColumn>
          <TableColumn>库存数量</TableColumn>
          <TableColumn>库存预警</TableColumn>
          <TableColumn>修改</TableColumn>
        </TableHeader>
        <TableBody
          items={list.items}
        >
          {(material: Material) => {
            return (
              <TableRow key={material.id}>
                <TableCell>{material.serialNum}</TableCell>
                <TableCell>{material.name}</TableCell>
                <TableCell>{material.category}</TableCell>
                <TableCell>{material.count}</TableCell>
                <TableCell>{material.inventoryCountAlert}</TableCell>
                <TableCell>
                  <div className="relative flex items-center gap-2">
                    <Tooltip content="修改物料">
                      <span className="text-lg text-default-400 cursor-pointer active:opacity-50">
                        <ModifyModalEditIcon  {...material}/>
                      </span>
                    </Tooltip>
                    <Tooltip color={"danger"} content="删除物料">
                      <span className="text-lg text-danger cursor-pointer active:opacity-50">
                        <DeleteModalDeleteIcon {...material}/>
                      </span>
                    </Tooltip>
                  </div>
                </TableCell>
              </TableRow>
            )
          }}
        </TableBody>
      </Table>
    </>
  )
}
