'use client'

import React, {FormEvent} from "react";
import {useRouter} from "next/navigation";
import myFetch from "@/app/myFetch";
import Res from "@/app/dto/res";
import {Button, Modal, ModalContent, ModalFooter, ModalHeader, useDisclosure} from "@nextui-org/react";

export function LoginForm() {
  const router = useRouter();
  const modal = useDisclosure();

  async function onSubmitHandler(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()

    const formData = new FormData(event.currentTarget)
    const username = formData.get('username')
    const password = formData.get('password')

    myFetch('/api/login', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({username: username, password: password}),
    })
      .then((resp)=>resp.json())
      .then((resp:Res<string>)=>{
        if (resp.successCode == "success") {
          router.push('/');
        } else if (resp.msg == "still default password") {
          modal.onOpen()
        } else {
          alert(resp.msg);
        }
      })
  }

  function resetPasswordHandler(e: React.FormEvent<HTMLFormElement>){
    e.preventDefault();
    router.push('/login/reset');
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
          <div className={"my-1.5"}>密码：</div>
          <input
            required
            className="box-border py-1.5 pl-2.5 my2 rounded-xl border border-gray-300 w-full"
            type="password"
            name="password"/>
        </div>
        <button
          className={"hover:bg-neutral-800 bg-black w-full py-2 px-4 mt-5 text-center text-white font-medium rounded-full transition duration-100"}
          type="submit"
        >
          登录
        </button>
      </form>
      <Modal backdrop={"blur"} isOpen={modal.isOpen} onOpenChange={modal.onOpenChange}>
        <ModalContent>
          {(onClose) => (
            <>
              <form onSubmit={resetPasswordHandler}>
                <ModalHeader className="flex flex-col gap-1">您正在使用默认密码，为了账号安全，请设置新密码</ModalHeader>
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
