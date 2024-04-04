import {DeleteIcon} from "@nextui-org/shared-icons";
import React from "react";
import {Button, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader, useDisclosure} from "@nextui-org/react";
import Material from "@/app/dto/material";
import Product from "@/app/dto/product";
import ProductMaterialRel from "@/app/dto/productMaterialRel";

export default function DeleteModalDeleteIcon(rel: ProductMaterialRel) {
  const deleteModal = useDisclosure();

  function remove(e: React.FormEvent<HTMLFormElement>, relId: number) {
    e.preventDefault()

    fetch(`/api/bom/rel?id=${relId}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json"
      }
    })
      .then(response => response.json())
      .then(data => {
        if (data["successCode"]==="success") {
          deleteModal.onClose
          window.location.reload()
        }else {
          alert(data["msg"])
        }
      })
  }

  return (
    <>
      <DeleteIcon onClick={deleteModal.onOpen}/>
      <Modal isOpen={deleteModal.isOpen} onOpenChange={deleteModal.onOpenChange}>
        <ModalContent>
          {(onClose) => (
            <>
              <form onSubmit={(e) => remove(e, rel.id)}>
                <ModalHeader className="flex flex-col gap-1">您确定要从BOM中删除该物料吗</ModalHeader>
                <ModalBody>
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
