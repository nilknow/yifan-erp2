import {DeleteIcon} from "@nextui-org/shared-icons";
import React from "react";
import {Button, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader, useDisclosure} from "@nextui-org/react";
import Material from "@/lib/dto/material";
import myFetch from "@/lib/myFetch";

export default function DeleteModalDeleteIcon(material: Material) {
  const deleteModal = useDisclosure();

  function remove(e: React.FormEvent<HTMLFormElement>, materialId: number) {
    e.preventDefault()

    myFetch(`/api/material?materialId=${materialId}&source=button`, {
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
      <Modal backdrop={"blur"} isOpen={deleteModal.isOpen} onOpenChange={deleteModal.onOpenChange}>
        <ModalContent>
          {(onClose) => (
            <>
              <form onSubmit={(e) => remove(e, material.id)}>
                <ModalHeader className="flex flex-col gap-1">您确定要删除该物料吗</ModalHeader>
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
