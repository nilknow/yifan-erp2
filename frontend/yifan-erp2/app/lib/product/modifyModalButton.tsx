import {Button, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader, useDisclosure} from "@nextui-org/react";
import React from "react";
import Material from "@/app/dto/material";
import {EditIcon} from "@nextui-org/shared-icons";
import {Input} from "@nextui-org/input";
import Product from "@/app/dto/product";

export default function ModifyModalEditIcon(product: Product) {
  const modifyModal = useDisclosure();

  function modify(e: React.FormEvent<HTMLFormElement>, product: Product) {
    e.preventDefault();

    const formData = new FormData(e.currentTarget)
    const serialNum = formData.get('serialNum')
    const name = formData.get('name')
    const categoryName = formData.get('categoryName')
    const count = formData.get('count')
    const description=formData.get('description')
    const unit=formData.get('unit')

    fetch(`/api/product`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        id: product.id,
        serialNum,
        name,
        categoryName,
        count,
        description,
        unit,
      })
    })
      .then(res => res.json())
      .then(data => {
        const successCode = data["successCode"]
        if ("success" !== successCode) {
          alert(data["msg"]);
        } else {
          modifyModal.onClose();
          window.location.reload()
        }
      })
  }

  return (
    <>
      <EditIcon onClick={modifyModal.onOpen}/>
      <Modal isOpen={modifyModal.isOpen} onOpenChange={modifyModal.onOpenChange}>
        <ModalContent>
          {(onClose) => (
            <>
              <form onSubmit={(e) => modify(e, product)}>
                <ModalHeader className="flex flex-col gap-1">修改产品</ModalHeader>
                <ModalBody>
                  <Input label={"产品编号"} name={"serialNum"} defaultValue={product.serialNum}/>
                  <Input label={"产品名称"} name={"name"} defaultValue={product.name}/>
                  <Input label={"产品描述"} name={"description"} defaultValue={product.description}/>
                  <Input label={"产品分类"} name={"categoryName"} defaultValue={product.category.name}/>
                  <Input label={"产品库存"} name={"count"} type={"number"}
                         defaultValue={product.count.toString()}/>
                  <Input label={"产品单位"} name={"unit"} defaultValue={product.unit}/>
                </ModalBody>
                <ModalFooter>
                  <Button type={"submit"}>确定</Button>
                  <Button type={"button"} onClick={onClose}>取消</Button>
                </ModalFooter>
              </form>
            </>
          )}
        </ModalContent>
      </Modal>
    </>
  )
}
