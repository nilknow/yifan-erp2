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
import {DeleteIcon, EditIcon} from "@nextui-org/shared-icons";

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

  return (
    <>
      <div>
        <form action="/material/list" method="get" encType="multipart/form-data">
          <input type="text" name="name" placeholder="输入物料名称"/>
          <button type="submit">搜索</button>
        </form>
      </div>
      <br/>
      <Table removeWrapper isStriped aria-label="material list table">
        <TableHeader>
          <TableColumn>编号
            <div className="arrow-box">
              <div className="arrow-up"></div>
              <div className="arrow-between"></div>
              <div className="arrow-down"></div>
            </div>
          </TableColumn>
          <TableColumn>物料名
            <div className="arrow-box">
              <div className="arrow-up"></div>
              <div className="arrow-between"></div>
              <div className="arrow-down"></div>
            </div>
          </TableColumn>
          <TableColumn>分类
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
    </>
  )
}
