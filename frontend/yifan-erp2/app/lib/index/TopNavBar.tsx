export default function TopNavBar() {
  return (
    <div
      className={"flex-row"}>
      <a className={"text-gray-500 hover:bg-gray-500 hover:text-white px-4 py-4 text-left font-bold transition duration-300"} href="/">返回首页</a>
      <a className={"text-gray-500 hover:bg-gray-500 hover:text-white px-4 py-4 text-left font-bold transition duration-300"} href="/logout">登出</a>
    </div>
  )
}
