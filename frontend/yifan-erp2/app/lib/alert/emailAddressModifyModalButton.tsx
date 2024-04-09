'use client'
import {
  Button,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
  useDisclosure
} from "@nextui-org/react";
import {Input} from "@nextui-org/input";
import React from "react";
import Res from "@/app/dto/res";
import myFetch from "@/app/myFetch";

export default function EmailAddressModifyModalButton() {
  const modal = useDisclosure();

  async function changeEmailHandler(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault()
    const formData = new FormData(e.currentTarget);
    const emailAddress = formData.get("emailAddress");

    myFetch('/api/alert/email', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          address:emailAddress,
        })
      }
    ).then((res) => res.json())
      .then((data:Res<string>) => {
        if ("success" === data.successCode) {
          modal.onClose();
          window.location.reload();
        } else {
          alert(data.msg)
        }
      })
  }

  return (
    <>
      <Button size={"sm"} radius={"full"} onPress={modal.onOpen} color="default">
        修改邮箱
      </Button>
      <Modal backdrop={"blur"} isOpen={modal.isOpen} onOpenChange={modal.onOpenChange}>
        <ModalContent>
          {(onClose) => (
            <>
              <form onSubmit={changeEmailHandler}>
                <ModalHeader className="flex flex-col gap-1">Change you alert email recipient</ModalHeader>
                <ModalBody>
                  <Input
                    name={"emailAddress"}
                    required={true}
                    placeholder={"请输入新的邮件地址"}>
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
