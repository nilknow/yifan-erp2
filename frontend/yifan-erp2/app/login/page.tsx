import {LoginForm} from "@/app/lib/login/LoginForm";

export default function Page() {
  return (
    <div className={"h-screen bg-neutral-100"}>
      <div
        className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 bg-white rounded-lg p-10 w-[350px]">
        <h1 className={"text-2xl mb-5 font-bold"}>登录 66进销存</h1>
        <LoginForm></LoginForm>
      </div>
    </div>
  )
}
