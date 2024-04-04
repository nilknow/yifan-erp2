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
  useDisclosure
} from "@nextui-org/react";
import {PlusFilledIcon} from "@nextui-org/shared-icons";
import {Input} from "@nextui-org/input";
import React, {useEffect, useState} from "react";
import {useSearchParams} from "next/navigation";
import ProductMaterialRel from "@/app/dto/productMaterialRel";
import Material from "@/app/dto/material";
import Res from "@/app/dto/res";

export default function AddMaterialModalButton() {
  const modal = useDisclosure();
  const searchParams = useSearchParams();
  const productId = searchParams.get('productId');
  const [sortedProductMaterialRels, setSortedProductMaterialRels] = useState<ProductMaterialRel[]>([]);
  const [selectedMaterialType, setSelectedMaterialType] = useState<string>();
  const [materialTypes, setMaterialTypes] = useState<string[]>([]);
  const [materials, setMaterials] = useState<Material[]>([]);

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
      .then((data: Res<Material[]>) => {
        if (data.successCode !== "success") {
          alert(data.msg)
          return
        }
        setMaterials(data.body)
        setMaterialTypes(Array.from(new Set(data.body.map(material => material.category))))
      })
  }, [])

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

  function categoryChangeHandler(e: React.ChangeEvent<HTMLSelectElement>) {
    setSelectedMaterialType(e.target.value)
  }

  return (
    <>
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
    </>
  )
}
