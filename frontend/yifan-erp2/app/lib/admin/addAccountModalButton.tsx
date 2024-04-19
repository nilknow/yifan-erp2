import {Button, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader, useDisclosure} from "@nextui-org/react";
import {PlusFilledIcon} from "@nextui-org/shared-icons";
import {Input} from "@nextui-org/input";
import React from "react";
import myFetch from "@/app/myFetch";
import Res from "@/app/dto/res";
import Product from "@/app/dto/product";

export default function AddAccountModalButton(){
  const modal = useDisclosure();

  function addAccountHandler(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();

    const form = e.currentTarget;
    const formData = new FormData(form);
    let name = formData.get("name");
    let password = formData.get("password");

    myFetch("/api/admin/user", {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({name, password})
    })
      .then(data => data.json())
      .then((data: Res<Product[]>) => {
        if (data.successCode === 'success') {
          modal.onClose()
          window.location.reload()
        } else {
          alert(data.msg)
        }
      })
  }

  return (
    <>
      <Button size={"sm"} radius={"full"} onPress={modal.onOpen} color="default" endContent={<PlusFilledIcon/>}>
        新建账号
      </Button>
      <Modal backdrop={"blur"} isOpen={modal.isOpen} onOpenChange={modal.onOpenChange}>
        <ModalContent>
          {(onClose) => (
            <>
              <form onSubmit={addAccountHandler}>
                <ModalHeader className="flex flex-col gap-1">新建账号</ModalHeader>
                <ModalBody>
                  <Input label={"账号名称"}
                         name={"name"}
                         required={true}>
                  </Input>
                  <Input label={"账号密码"}
                         name={"password"}
                         required={true}>
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
