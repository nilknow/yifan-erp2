import {
  Button,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
  SelectItem, useDisclosure
} from "@nextui-org/react";
import {PlusFilledIcon} from "@nextui-org/shared-icons";
import {Input} from "@nextui-org/input";
import React from "react";
import Res from "@/app/dto/res";
import Material from "@/app/dto/material";

export default function BatchAddModalButton() {
  const modal = useDisclosure();

  async function batchAddHandler(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault(); // Prevent default form submission behavior

    let formData = e.currentTarget;
    fetch('/api/material/batch', {
      method: 'POST',
      body:new FormData (formData),
    })
      .then((res) => res.json())
      .then((data:Res<string>) => {
        if("success" === data.successCode){
          window.location.reload()
        }else{
          alert(data.msg)
        }
      })
  }

  return (
    <>
      <Button size={"sm"} radius={"full"} onPress={modal.onOpen} color="default" endContent={<PlusFilledIcon/>}>
        批量添加
      </Button>
      <Modal isOpen={modal.isOpen} onOpenChange={modal.onOpenChange}>
        <ModalContent>
          {(onClose) => (
            <>
              <form onSubmit={batchAddHandler}>
                <ModalHeader className="flex flex-col gap-1">批量添加物料</ModalHeader>

                <ModalBody>
                  <Button onClick={() => window.location.href = '/api/material/template'}>下载模板</Button>

                  <Input
                    label={"选择批量添加用的的Excel文件"}
                    name={"file"}
                    type={"file"}
                    required={true}
                    accept=".xlsx, .xls"
                    placeholder={"请上传批量添加Excel文件"}>
                  </Input>
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
