'use client'

import React, {FormEvent} from "react";
import {useRouter} from "next/navigation";
import myFetch from "@/app/myFetch";
import Res from "@/app/dto/res";
import {Button, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader, useDisclosure} from "@nextui-org/react";

export function PasswordResetForm() {
  const router = useRouter();
  const modal = useDisclosure();

  async function onSubmitHandler(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()

    const formData = new FormData(event.currentTarget)
    const username = formData.get('username')
    const password = formData.get('password')
    const newPassword = formData.get('newPassword')
    const newPasswordConfirm = formData.get('newPasswordConfirm')

    if (newPassword != newPasswordConfirm) {
      alert("请重新输入新密码，您的新密码和确认新密码不一致")
      return
    }

    myFetch('/api/login/reset', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({username: username, password: password, newPassword: newPassword}),
    }).then((resp) => resp.json())
      .then((resp: Res<string>) => {
        if (resp.successCode == "success") {
          modal.onOpen()
        } else {
          alert(resp.msg);
        }
      })
  }

  function confirmPasswordChangedHandler(e: React.FormEvent<HTMLFormElement>){
    e.preventDefault();
    router.push('/login');
  }

  return (
    <div>
      <form
        onSubmit={onSubmitHandler}
      >
        <div>
          <div className={"my-1.5"}>用户名：</div>
          <input
            required
            className="box-border py-1.5 pl-2.5 my2 rounded-xl border border-gray-300 w-full"
            type="text"
            name="username"/>
        </div>
        <div>
          <div className={"my-1.5"}>旧密码：</div>
          <input
            required
            className="box-border py-1.5 pl-2.5 my2 rounded-xl border border-gray-300 w-full"
            type="password"
            name="password"/>
        </div>
        <div>
          <div className={"my-1.5"}>新密码：</div>
          <input
            required
            className="box-border py-1.5 pl-2.5 my2 rounded-xl border border-gray-300 w-full"
            type="password"
            name="newPassword"/>
        </div>
        <div>
          <div className={"my-1.5"}>确认新密码：</div>
          <input
            required
            className="box-border py-1.5 pl-2.5 my2 rounded-xl border border-gray-300 w-full"
            type="password"
            name="newPasswordConfirm"/>
        </div>
        <button
          className={"hover:bg-neutral-800 bg-black w-full py-2 px-4 mt-5 text-center text-white font-medium rounded-full transition duration-100"}
          type="submit"
        >
          确定
        </button>
      </form>
      <Modal backdrop={"blur"} isOpen={modal.isOpen} onOpenChange={modal.onOpenChange}>
        <ModalContent>
          {(onClose) => (
            <>
              <form onSubmit={confirmPasswordChangedHandler}>
                <ModalHeader className="flex flex-col gap-1">您的密码已经被修改，请重新登录</ModalHeader>
                <ModalFooter>
                  <Button color="default" variant="light" onPress={onClose}>
                    取消
                  </Button>
                  <Button type="submit" color="default" onPress={onClose}>
                    确定
                  </Button>
                </ModalFooter>
              </form>
            </>
          )}
        </ModalContent>
      </Modal>
    </div>
  )
}
