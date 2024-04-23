import {Table, TableBody, TableCell, TableColumn, TableHeader, TableRow, Tooltip} from "@nextui-org/react";
import React, {useEffect} from "react";
import {Input} from "@nextui-org/input";
import {SearchIcon} from "@nextui-org/shared-icons";
import Product from "@/lib/dto/product";
import myFetch from "@/lib/myFetch";
import Res from "@/lib/dto/res";
import AddModalButton from "@/lib/product/addModalButton";
import ModifyModalEditIcon from "@/lib/product/modifyModalButton";
import DeleteModalDeleteIcon from "@/lib/product/deleteModalDeleteIcon";


export default function ManagableProductList() {
  const [products, setProducts] = React.useState<Product[]>([]);
  useEffect(() => {
    myFetch('/api/product/list')
      .then(response => response.json())
      .then((data: Res<Product[]>) => {
        if ("success" === data.successCode) {
          setProducts(data.body);
        } else {
          alert("获取成品列表失败，请稍后重试")
        }
      })
  }, [])

  async function search(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault()
    const formData = new FormData(e.currentTarget)
    let name = formData.get("name");
    if (name === null) {
      return
    }
    let response = await myFetch(`/api/product/list?name=${name}`)
    let data: Res<Product[]> = await response.json()
    if ("success" === data.successCode) {
      setProducts(data.body)
    } else {
      alert("查询失败，请稍后重试")
    }
  }

  async function keyUpSearch(e: React.KeyboardEvent<HTMLInputElement>) {
    e.preventDefault()

    let name = e.currentTarget.value;
    const response = await myFetch(`/api/product/list?name=${name}`);
    const data = await response.json()
    if ("success" === data.successCode) {
      setProducts(data.body)
    } else {
      alert("查询失败，请稍后重试")
    }
  }

  return (
    <div className={"mx-4"}>
      <div>
        <form onSubmit={search}>
          <div className="flex justify-between gap-3 items-end">
            <Input
              type="text" name="name" size={"sm"}
              onKeyUp={keyUpSearch}
              isClearable
              radius="lg"
              classNames={{
                label: "text-black/50 dark:text-white/90",
                input: [
                  "bg-transparent",
                  "text-black/90 dark:text-white/90",
                  "placeholder:text-default-700/50 dark:placeholder:text-white/60",
                ],
                innerWrapper: "bg-transparent",
                inputWrapper: [
                  "shadow-xl",
                  "bg-default-200/50",
                  "dark:bg-default/60",
                  "backdrop-blur-xl",
                  "backdrop-saturate-200",
                  "hover:bg-default-200/70",
                  "dark:hover:bg-default/70",
                  "group-data-[focused=true]:bg-default-200/50",
                  "dark:group-data-[focused=true]:bg-default/60",
                  "!cursor-text",
                ],
              }}
              placeholder="输入成品名称..."
              startContent={
                <SearchIcon
                  className="text-black/50 mb-0.5 dark:text-white/90 text-slate-400 pointer-events-none flex-shrink-0"/>
              }
            />
            <div className="flex gap-3">
              <AddModalButton></AddModalButton>
            </div>
          </div>
        </form>
      </div>
      <br/>
      <Table isStriped>
        <TableHeader>
          <TableColumn>成品编号</TableColumn>
          <TableColumn>成品名称</TableColumn>
          <TableColumn>成品描述</TableColumn>
          <TableColumn>成品分类</TableColumn>
          <TableColumn>成品库存</TableColumn>
          <TableColumn>成品单位</TableColumn>
          <TableColumn>管理</TableColumn>
        </TableHeader>
        <TableBody>
          {products.map(product => (
            <TableRow key={product.id}>
              <TableCell>{product.serialNum}</TableCell>
              <TableCell>{product.name}</TableCell>
              <TableCell>{product.description}</TableCell>
              <TableCell>{product.category.name}</TableCell>
              <TableCell>{product.count}</TableCell>
              <TableCell>{product.unit}</TableCell>
              <TableCell>
                <div className="relative flex items-center gap-2">
                  <Tooltip content="修改成品">
                    <span className="text-lg text-default-400 cursor-pointer active:opacity-50">
                      <ModifyModalEditIcon  {...product}/>
                    </span>
                  </Tooltip>
                  <Tooltip color={"danger"} content="删除成品">
                    <span className="text-lg text-danger cursor-pointer active:opacity-50">
                      <DeleteModalDeleteIcon {...product}/>
                    </span>
                  </Tooltip>
                </div>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  )
}
