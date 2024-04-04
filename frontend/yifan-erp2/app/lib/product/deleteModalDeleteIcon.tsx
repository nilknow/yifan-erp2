import {DeleteIcon} from "@nextui-org/shared-icons";
import React from "react";
import {Button, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader, useDisclosure} from "@nextui-org/react";
import Material from "@/app/dto/material";
import Product from "@/app/dto/product";

export default function DeleteModalDeleteIcon(product: Product) {
  const deleteModal = useDisclosure();

  function remove(e: React.FormEvent<HTMLFormElement>, productId: string) {
    e.preventDefault()

    fetch(`/api/product/${productId}`, {
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
              <form onSubmit={(e) => remove(e, product.id)}>
                <ModalHeader className="flex flex-col gap-1">您确定要删除该产品吗</ModalHeader>
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
