export default function TopNavBar() {
  return (
    <div className={"flex-row"}>
      <a href={"/"}>
        <img src={"/66_long.png"} className={"w-8 h-8 m-5 inline"}/>
      </a>
      <a href="/"
         className={"text-gray-500 hover:bg-neutral-700 hover:text-white px-4 py-4 text-left font-bold transition duration-300"}
      >返回首页</a>
      <a href="/logout"
         className={"text-gray-500 hover:bg-neutral-700 hover:text-white px-4 py-4 text-left font-bold transition duration-300"}
      >登出</a>
    </div>
  )
}
