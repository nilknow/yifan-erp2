import {
  Button,
  Table,
  TableBody,
  TableCell,
  TableColumn,
  TableHeader,
  TableRow,
  Tooltip
} from "@nextui-org/react";
import ModifyModalEditIcon from "@/lib/material/modifyModalButton";
import DeleteModalDeleteIcon from "@/lib/material/deleteModalDeleteIcon";
import React, {useEffect, useState} from "react";
import Material from "@/lib/dto/material";
import Res from "@/lib/dto/res";
import {Input} from "@nextui-org/input";
import {SearchIcon} from "@nextui-org/shared-icons";
import myFetch from "@/lib/myFetch";

export default function MaterialTable({children}: { children: React.ReactNode }) {
  const [materials, setMaterials] = useState<Material[]>([]);
  const [materialTypes, setMaterialTypes] = React.useState<string[]>([]);
  const [selectedType, setSelectedType] = React.useState<string>('');
  const [updateTimeOrder, setUpdateTimeOrder] = React.useState<string>('desc')
  const [serialNumOrder, setSerialNumOrder] = React.useState<string>('')
  const [nameOrder, setNameOrder] = React.useState<string>('')
  const [categoryOrder, setCategoryOrder] = React.useState<string>('')
  const [nameFilter, setNameFilter] = React.useState<string>('')

  useEffect(() => {
    console.log("effected>>>>>>>>")
    myFetch(`/api/material/list?name=${nameFilter}
    &sort=updateTimestamp,${updateTimeOrder}
    &sort=serialNum,${serialNumOrder}
    &sort=name,${nameOrder}
    &sort=category,${categoryOrder}`)
      .then((res) => res.json())
      .then((data: Res<Material[]>) => {
        if ("success" === data.successCode) {
          setMaterials(data.body)
          setMaterialTypes(Array.from(new Set(data.body.map(material => material.category))))
        } else {
          alert("查询失败，请稍后重试")
        }
      })
  }, [selectedType, updateTimeOrder, serialNumOrder, nameOrder, categoryOrder, nameFilter])

  function serialNumClickHandler() {
    if (serialNumOrder) {
      if (serialNumOrder === 'desc') {
        setSerialNumOrder('asc');
      } else {
        setSerialNumOrder('desc');
      }
    } else {
      setSerialNumOrder('asc');
    }
  }

  function nameClickHandler() {
    if (nameOrder) {
      if (nameOrder === 'desc') {
        setNameOrder('asc');
      } else {
        setNameOrder('desc');
      }
    } else {
      setNameOrder('asc');
    }
  }

  function categoryClickHandler() {
    if (categoryOrder) {
      if (categoryOrder === 'desc') {
        setCategoryOrder('asc');
      } else {
        setCategoryOrder('desc');
      }
    } else {
      setCategoryOrder('asc');
    }
  }

  const columns = [
    {
      key: "serialNum",
      label: "编号",
      allowsSorting: true,
      sortHandler: serialNumClickHandler
    },
    {
      key: "name",
      label: "物料名",
      // allowsSorting: true,
      sortHandler: nameClickHandler
    },
    {
      key: "category",
      label: "分类",
      // allowsSorting: true,
      // sortHandler: categoryClickHandler
    },
    {
      key: "count",
      label: "库存数量",
    },
    {
      key: "inventoryCountAlert",
      label: "库存预警",
    },
    {
      key: "modify",
      label: "修改",
    },
  ];

  return (
    <>
      <div>
        <form>
          <div className="flex justify-between gap-3 items-end">
            <Input
              type="text" name="name" size={"sm"}
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
              <Button size={"sm"} radius={"full"} color="default"
                      onClick={() => window.location.href = '/material/batch'}>批量处理</Button>
            </div>
          </div>
        </form>
      </div>
      <br/>
      <Table
        removeWrapper
        isStriped
        aria-label="material list table"
      >
        <TableHeader columns={columns}>
          {
            (column) => (
                <TableColumn
                  key={column.key}
                  allowsSorting={column.allowsSorting}
                  onClick={column.sortHandler}>
                  {column.label}
                </TableColumn>
            )
          }
        </TableHeader>
        <TableBody
          items={materials}
        >
          {(material: Material) => {
            console.log(material)
            return (
              <TableRow key={material.id}>
                <TableCell>{material.serialNum}</TableCell>
                <TableCell>{material.name}</TableCell>
                <TableCell>{material.category}</TableCell>
                <TableCell
                  className={material.count <= material.inventoryCountAlert ? "text-[#f31260]" : ""}
                >
                  {material.count <= material.inventoryCountAlert
                    ? (
                      <Tooltip content={`库存数量达到库存预警下限: ${material.inventoryCountAlert}`}>
                        <span>{material.count}</span>
                      </Tooltip>
                    )
                    :
                    <span>{material.count}</span>
                  }
                </TableCell>
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
