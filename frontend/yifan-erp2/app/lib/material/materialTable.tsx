'use client'
import React, {useEffect, useState} from "react";
import {
  Table,
  TableHeader,
  TableBody,
  TableColumn,
  TableRow,
  TableCell, Button, Tooltip, useDisclosure, Modal, ModalContent, ModalHeader, ModalBody, ModalFooter
} from "@nextui-org/react";
import {DeleteIcon, EditIcon, SearchIcon} from "@nextui-org/shared-icons";
import {Input} from "@nextui-org/input";
import Material from "@/app/dto/material";
import DeleteModalDeleteIcon from "@/app/lib/material/deleteModalDeleteIcon";
import ModifyModalEditIcon from "@/app/lib/material/modifyModalButton";
import AddModalButton from "@/app/lib/material/addModalButton";
import Res from "@/app/dto/res";
import Product from "@/app/dto/product";
import BatchAddModalButton from "@/app/lib/material/batchAddModalButton";

export default function MaterialTable() {
  const [sortedMaterials, setSortedMaterials] = useState<Material[]>([]);
  useEffect(() => {
    fetch('/api/material/list')
      .then((res) => res.json())
      .then((data:Res<Material[]>) => {
        if("success" === data.successCode){
          setSortedMaterials(data.body)
        }else{
          alert("查询失败，请稍后重试")
        }
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
    let data:Res<Material[]> = await response.json()
    if("success" === data.successCode){
      setSortedMaterials(data.body)
    }else{
      alert("查询失败，请稍后重试")
    }
  }

  async function keyUpSearch(e: React.KeyboardEvent<HTMLInputElement>) {
    e.preventDefault()

    let name = e.currentTarget.value;
    const response = await fetch(`/api/material/list?name=${name}`);
    let data:Res<Material[]> = await response.json()
    if("success" === data.successCode){
      setSortedMaterials(data.body)
    }else{
      alert("查询失败，请稍后重试")
    }
  }

  return (
    <div className={"mx-4"}>
      <div>
        <form onSubmit={search}>
          <div className="flex justify-between gap-3 items-end">
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
            <div className="flex gap-3">
              <AddModalButton></AddModalButton>
              <BatchAddModalButton></BatchAddModalButton>
            </div>
          </div>
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
