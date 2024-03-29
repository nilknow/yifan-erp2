export default function TopNavBar() {
  return (
    <div className={"flex-row"}>
      <a href={"/"}>
        <img src={"/66_long.png"} className={"w-8 h-8 m-5 inline"}/>
      </a>
      <a href="/yifan-erp2/public"
         className={"text-neutral-500 hover:bg-neutral-700 hover:text-white p-3 text-left font-bold transition duration-100"}
      >返回首页</a>
      <a href="/logout"
         className={"text-neutral-500 hover:bg-neutral-700 hover:text-white p-3 text-left font-bold transition duration-100"}
      >登出</a>
    </div>
  )
}
