'use client'
import {FormEvent} from "react";
import {useRouter} from "next/navigation";

export function LoginForm() {
  const router=useRouter();
  async function handleSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault()
    const formData = new FormData(e.currentTarget)
    const username = formData.get('username')
    const password = formData.get('password')

    const response = await fetch('/api/login', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({username: username, password: password}),
    })

    if (response.ok) {
      response.text().then((respText) => {
          if (respText) {
            localStorage.setItem('isLoggedIn', 'true');
            localStorage.setItem('Authorization', respText)
            console.log('logged in!!')
            router.push('/');
          }
        }
      );
    } else {
      //todo login fail
    }
  }

  return (
    <form onSubmit={handleSubmit}>
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
        className={"hover:bg-neutral-800 bg-black w-full py-2 px-4 mt-5 text-center text-white font-medium rounded-full transition duration-300"}
        type="submit">
        登录
      </button>
    </form>
  )
}
