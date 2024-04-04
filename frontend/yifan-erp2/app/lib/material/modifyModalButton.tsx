import {Button, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader, useDisclosure} from "@nextui-org/react";
import React from "react";
import Material from "@/app/dto/material";
import {EditIcon} from "@nextui-org/shared-icons";
import {Input} from "@nextui-org/input";

export default function ModifyModalEditIcon(material: Material) {
  const modifyModal = useDisclosure();

  function modify(e: React.FormEvent<HTMLFormElement>, material: Material) {
    e.preventDefault();

    const formData = new FormData(e.currentTarget)
    const serialNum = formData.get('serialNum')
    const name = formData.get('name')
    const category = formData.get('category')
    const count = formData.get('count')
    const inventoryCountAlert = formData.get('inventoryCountAlert')

    fetch(`/api/material`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        id: material.id,
        serialNum,
        name,
        category,
        count,
        inventoryCountAlert,
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
              <form onSubmit={(e) => modify(e, material)}>
                <ModalHeader className="flex flex-col gap-1">修改物料</ModalHeader>
                <ModalBody>
                  <Input label={"编号"} name={"serialNum"} defaultValue={material.serialNum}/>
                  <Input label={"物料名"} name={"name"} defaultValue={material.name}/>
                  <Input label={"分类"} name={"category"} defaultValue={material.category}/>
                  <Input label={"库存数量"} name={"count"} type={"number"}
                         defaultValue={material.count.toString()}/>
                  <Input label={"库存预警"} name={"inventoryCountAlert"} type={"number"}
                         defaultValue={material.inventoryCountAlert.toString()}/>
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
