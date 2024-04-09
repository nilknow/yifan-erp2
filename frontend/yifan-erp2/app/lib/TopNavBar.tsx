'use client'

import {useRouter} from "next/navigation";
import {Image} from "@nextui-org/react";
import myFetch from "@/app/myFetch";

export default function TopNavBar() {
  let router = useRouter();

  //fix it
  async function logoutHandler(e:any) {
    e.preventDefault()
    await myFetch('/api/logout', {
      method: 'POST',
    }).then(()=>{
      router.push('/')
    })
    return;
  }

  return (
    <div className={"flex-row"}>
      <a href={"/"}>
        <img src={"/66_long.png"} className={"w-8 h-8 m-5 inline"} loading={"eager"}/>
      </a>
      <a href="/"
         className={"text-neutral-500 hover:bg-neutral-700 hover:text-white p-3 text-left font-bold transition duration-100"}
      >返回首页</a>
      <a href="/logout"
         onClick={logoutHandler}
         className={"text-neutral-500 hover:bg-neutral-700 hover:text-white p-3 text-left font-bold transition duration-100"}
      >登出</a>
    </div>
  )
}
