'use client'
import React, {useEffect, useState} from "react";
import {
  Table,
  TableHeader,
  TableBody,
  TableColumn,
  TableRow,
  TableCell, Button, Tooltip
} from "@nextui-org/react";
import {DeleteIcon, EditIcon, SearchIcon} from "@nextui-org/shared-icons";
import {Input} from "@nextui-org/input";

interface Material {
  id: string;
  serialNum: string;
  name: string;
  category: string;
  count: number;
  inventoryCountAlert: number;
}

export default function Form() {
  const [sortedMaterials, setSortedMaterials] = useState<Material[]>([]);
  useEffect(() => {
    fetch('/api/material/list')
      .then((res) => res.json())
      .then((data) => {
        setSortedMaterials(data)
      })
  }, [])

  async function search(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault()
    const formData = new FormData(e.currentTarget)
    const name = formData.get('name')
    if (name === null) {
      return
    }
    const response = await fetch(`/api/material/list?name=${name}`)
    const data = await response.json()
    setSortedMaterials(data)
  }

  async function keyUpSearch(e: React.KeyboardEvent<HTMLInputElement>) {
    e.preventDefault()

    let name = e.currentTarget.value;
    const response = await fetch(`/api/material/list?name=${name}`);
    const data = await response.json()
    setSortedMaterials(data)
  }

  return (
    <div className={"mx-4"}>
      <div>
        <form onSubmit={search}>
          <Input
            type="text" name="name" size={"sm"}
            onKeyUp={keyUpSearch}
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
        </form>
      </div>
      <br/>
      <Table removeWrapper isStriped aria-label="material list table">
        <TableHeader>
          <TableColumn allowsSorting>编号
            <div className="arrow-box">
              <div className="arrow-up"></div>
              <div className="arrow-between"></div>
              <div className="arrow-down"></div>
            </div>
          </TableColumn>
          <TableColumn allowsSorting>物料名
            <div className="arrow-box">
              <div className="arrow-up"></div>
              <div className="arrow-between"></div>
              <div className="arrow-down"></div>
            </div>
          </TableColumn>
          <TableColumn allowsSorting>分类
            <div className="arrow-box">
              <div className="arrow-up"></div>
              <div className="arrow-between"></div>
              <div className="arrow-down"></div>
            </div>
          </TableColumn>
          <TableColumn>库存数量</TableColumn>
          <TableColumn>库存预警</TableColumn>
          <TableColumn>修改</TableColumn>
        </TableHeader>
        <TableBody
          // emptyContent={"请添加物料，目前还没有任何物料"}
        >
          {sortedMaterials.map((material) => (
            <TableRow key={material.id}>
              <TableCell>{material.id}</TableCell>
              <TableCell>{material.name}</TableCell>
              <TableCell>{material.category}</TableCell>
              <TableCell>{material.count}</TableCell>
              <TableCell>{material.inventoryCountAlert}</TableCell>
              <TableCell>
                <div className="relative flex items-center gap-2">
                  <Tooltip content="修改物料">
                    <span className="text-lg text-default-400 cursor-pointer active:opacity-50">
                      <EditIcon/>
                    </span>
                  </Tooltip>
                  <Tooltip color={"danger"} content="删除物料">
                    <span className="text-lg text-danger cursor-pointer active:opacity-50">
                      <DeleteIcon/>
                    </span>
                  </Tooltip>
                </div>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
      <form action="#" id="full-update-popup" className="popup hidden">
        <div id="to-add-table"></div>
        <div id="to-delete-table"></div>
        <div id="to-update-table"></div>
        <button type="submit" id="full-update-submit-btn" className="warn">Submit</button>
        <button type="button" id="full-update-cancel-btn">Cancel</button>
      </form>
      <form action="#" id="popup" className="popup hidden">
        <h2>FBI WARNING!!!</h2>
        You sure you want to clean all material data? You cannot undo it after cleaning.
        <br/>
        <button id="remove-all-btn" className="warn">确认</button>
        <button id="remove-all-cancel-btn">取消</button>
      </form>

      <form action="#" id="remove-one-popup" className="popup hidden">
        <h2>FBI WARNING!!!</h2>
        You sure you want to clean this material data? You cannot undo it after cleaning.
        <br/>
        <button id="remove-one-btn" className="warn">确认</button>
        <button id="remove-one-cancel-btn">取消</button>
      </form>
    </div>
  )
}
