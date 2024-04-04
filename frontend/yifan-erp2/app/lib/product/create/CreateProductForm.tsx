'use client'
import {Input} from "@nextui-org/input";
import {
  Autocomplete,
  Button,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
  SelectItem, useDisclosure
} from "@nextui-org/react";
import React, {useEffect} from "react";
import Category from "@/app/dto/category";
import Res from "@/app/dto/res";

export default function CreateProductFrom() {
  const modal = useDisclosure();
  const [categories, setCategories] = React.useState<string[]>([]);
  useEffect(() => {
    fetch('/api/product_category/list')
      .then((res) => res.json())
      .then((data: Res<Category[]>) => {
        if ("success" === data.successCode) {
          setCategories(data.body.map(category => category.name));
        }
      })
  }, [])

  async function createProductHandler(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault()

    const formData = new FormData(e.currentTarget)
    let serialNum = formData.get("serialNum");
    let name = formData.get("name");
    let description = formData.get("description");
    let category = formData.get("category");
    let unit = formData.get("unit");
    // let image = formData.get("image");

    await fetch('/api/product/create', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({serialNum, name, description, category, unit})
    })
  }

  return (
    <div className={"mx-4"}>
      <form onSubmit={createProductHandler}>
        <Input label={"产品编号"}
               name={"serialNum"}
               required={true}
               placeholder={"请输入产品编号（必须唯一）"}>
        </Input>
        <Input label={"产品名称"}
               name={"name"}
               required={true}
               placeholder={"请输入产品名称"}>
        </Input>
        <Input label={"产品描述"}
               name={"description"}
               required={false}
               placeholder={"请输入产品描述"}>
        </Input>
        {/*<Input label={"产品图片"}*/}
        {/*       name={"image"}*/}
        {/*       required={false}*/}
        {/*       placeholder={"请输入产品图片"}>*/}
        {/*</Input>*/}
        <Autocomplete
          type="text"
          placeholder={"请选择或输入产品分类"}
          label={"产品分类"}
          name={"category"}
          required={true}>
          {categories.map((item, index) => (
            <SelectItem key={index}>{item}</SelectItem>
          ))}
        </Autocomplete>
        <Input label={"产品单位"}
               name={"unit"}
               required={true}
               placeholder={"请输入产品单位"}
               defaultValue={"个"}>
        </Input>

        <Button type="submit" color="default">
          添加
        </Button>
      </form>
    </div>
  )
}
