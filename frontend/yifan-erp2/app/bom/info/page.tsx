'use client'
import {
  Button,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
  Select,
  SelectItem,
  Table,
  TableBody,
  TableCell,
  TableColumn,
  TableHeader,
  TableRow,
  Tooltip,
  useDisclosure
} from "@nextui-org/react";
import React, {Suspense, useEffect, useState} from "react";
import {DeleteIcon, PlusFilledIcon, SearchIcon} from "@nextui-org/shared-icons";
import {Input} from "@nextui-org/input";
import Material from "@/app/dto/material";
import {useSearchParams} from "next/navigation";
import ProductMaterialRel from "@/app/dto/productMaterialRel";
import Res from "@/app/dto/res";


export default function Page() {
  const searchParams = useSearchParams();
  const productId = searchParams.get('productId');
  const modal = useDisclosure();
  const [sortedProductMaterialRels, setSortedProductMaterialRels] = useState<ProductMaterialRel[]>([]);
  const [materialTypes, setMaterialTypes] = useState<string[]>([]);
  const [materials, setMaterials] = useState<Material[]>([]);
  const [selectedMaterialType, setSelectedMaterialType] = useState<string>();

  useEffect(() => {
    fetch(`/api/bom/info?productId=${productId}`)
      .then((res) => res.json())
      .then((data) => {
        setSortedProductMaterialRels(data)
      })
  }, [])

  useEffect(() => {
    fetch('/api/material/list')
      .then((res) => res.json())
      .then((data:Res<Material[]>) => {
        if(data.successCode !== "success"){
          alert(data.msg)
          return
        }
        setMaterials(data.body)
        setMaterialTypes(Array.from(new Set(data.body.map(material => material.category))))
      })
  }, [])

  function categoryChangeHandler(e: React.ChangeEvent<HTMLSelectElement>) {
    setSelectedMaterialType(e.target.value)
  }

  async function addMaterialHandler(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault()
    const form = e.currentTarget;
    const formData = new FormData(form);
    let materialId = formData.get("materialId");
    let materialCount = formData.get("materialCount");
    const productId = searchParams.get('productId');

    let req = {
      materialId,
      productId,
      materialCount
    };
    const response = await fetch('/api/bom/add', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(req)
    });
    const data = await response.json()
    const successCode = data["successCode"]
    if ("success" === successCode) {
      setSortedProductMaterialRels(data["body"]);
    } else {
      alert(data["msg"])
    }
  }

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
          <Button onPress={modal.onOpen} color="default" endContent={<PlusFilledIcon/>}>
            添加物料
          </Button>
          <Modal isOpen={modal.isOpen} onOpenChange={modal.onOpenChange}>
            <ModalContent>
              {(onClose) => (
                <>
                  <form onSubmit={addMaterialHandler}>
                    <ModalHeader className="flex flex-col gap-1">添加物料</ModalHeader>
                    <ModalBody>
                      <Select
                        label={"选择分类"}
                        onChange={categoryChangeHandler}>
                        {materialTypes.map((materialType) => (
                          <SelectItem key={materialType}>
                            {materialType}
                          </SelectItem>
                        ))}
                      </Select>
                      <Select label={"选择物料"} name={"materialId"}>
                        {materials
                          .filter((material) => material.category === selectedMaterialType)
                          .map((material) => (
                            <SelectItem key={material.id}>
                              {material.name}
                            </SelectItem>
                          ))}
                      </Select>
                      <Input type={"number"} name={"materialCount"} defaultValue={"1"}
                             label={"物料数目"}/>
                    </ModalBody>
                    <ModalFooter>
                      <Button color="default" variant="light" onPress={onClose}>
                        取消
                      </Button>
                      <Button type="submit" color="default" onPress={onClose}>
                        添加
                      </Button>
                    </ModalFooter>
                  </form>
                </>
              )}
            </ModalContent>
          </Modal>
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
        <TableBody emptyContent={"该产品目前没有BOM，请点击右上角\"添加物料\"按钮添加BOM需要的物料"}>
          {sortedProductMaterialRels.map((materialInfo) => (
            <TableRow key={materialInfo.material.id}>
              <TableCell>{materialInfo.material.id}</TableCell>
              <TableCell>{materialInfo.material.category}</TableCell>
              <TableCell>{materialInfo.material.name}</TableCell>
              <TableCell>{materialInfo.materialCount}</TableCell>
              <TableCell>
                <div className="relative flex items-center gap-2">
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
    </div>
  )
}
