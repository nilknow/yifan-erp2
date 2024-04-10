'use client'
import {
  Link,
  Table,
  TableBody,
  TableCell,
  TableColumn,
  TableHeader,
  TableRow,
  Tooltip
} from "@nextui-org/react";
import {Input} from "@nextui-org/input";
import {DeleteIcon, EditIcon, EyeIcon, SearchIcon} from "@nextui-org/shared-icons";
import React, {useEffect, useState} from "react";
import {useRouter} from "next/navigation";
import Product from "@/app/dto/product";
import myFetch from "@/app/myFetch";

export default function Page() {
  let router = useRouter();

  async function search(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault()

    const formData = new FormData(e.currentTarget)
    const name = formData.get('name')
    if (name === null) {
      return
    }
    const response = await myFetch(`/api/bom/list?name=${name}`)
    const data = await response.json()
    setSortedProducts(data)
  }

  async function keyUpSearch(e: React.KeyboardEvent<HTMLInputElement>) {
    e.preventDefault()

    let name = e.currentTarget.value;
    const response = await myFetch(`/api/bom/list?name=${name}`);
    const data = await response.json()
    setSortedProducts(data)
  }

  const [sortedProducts, setSortedProducts] = useState<Product[]>([]);
  useEffect(() => {
    myFetch('/api/bom/list')
      .then((res) => res.json())
      .then((data) => {
        setSortedProducts(data)
      })
  }, [])

  async function deleteBom(e: React.MouseEvent<SVGSVGElement>,productId:string) {
    //todo replace productId
    e.preventDefault()
    const response = await myFetch(`/api/bom/delete?productId=${productId}`, {
      method: 'DELETE',
    })
    if (response.status === 200) {
      router.refresh()
    }
  }

  return (
    <div className={"mx-4"}>
      <div>
        <form onSubmit={search}>
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
        </form>
      </div>
      <br/>
      <Table isStriped>
        <TableHeader>
          <TableColumn>BOM编号</TableColumn>
          <TableColumn>成品名</TableColumn>
          <TableColumn>修改</TableColumn>
        </TableHeader>
        <TableBody>
          {sortedProducts.map((product) => (
            <TableRow key={product.id}>
              <TableCell>{product.name}</TableCell>
              <TableCell>{product.name}</TableCell>
              <TableCell>
                <div className="relative flex items-center gap-2">
                  <Tooltip content="查看BOM">
                    <span className="text-lg text-default-400 cursor-pointer active:opacity-50">
                      <Link href={"/bom/info?productId=" + product.id}>
                        <EyeIcon/>
                      </Link>
                    </span>
                  </Tooltip>
                  <Tooltip content="修改BOM">
                    <span className="text-lg text-default-400 cursor-pointer active:opacity-50">
                      <EditIcon/>
                    </span>
                  </Tooltip>
                  <Tooltip color={"danger"} content="删除BOM">
                    <span className="text-lg text-danger cursor-pointer active:opacity-50">
                      <DeleteIcon onClick={(e)=>deleteBom(e,product.id)}/>
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
