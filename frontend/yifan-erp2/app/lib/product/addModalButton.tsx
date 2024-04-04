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
import {PlusFilledIcon} from "@nextui-org/shared-icons";
import React, {useEffect} from "react";
import {Input} from "@nextui-org/input";
import Product from "@/app/dto/product";
import Res from "@/app/dto/res";
import Category from "@/app/dto/category";

export default function AddModalButton() {
  const modal = useDisclosure();
  const [categories, setCategories] = React.useState<Category[]>([]);
  const [products, setProducts] = React.useState<Product[]>([]);

  useEffect(() => {
    fetch(`/api/product_category/list`)
      .then((res) => res.json())
      .then((data: Res<Category[]>) => {
        if (data.successCode === 'success') {
          setCategories(data.body);
          modal.onClose()
        } else {
          alert(data.msg)
        }
      })
  }, [])

  async function addProductHandler(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();

    const form = e.currentTarget;
    const formData = new FormData(form);
    let serialNum = formData.get("serialNum");
    let name = formData.get("name");
    let description = formData.get("description");
    let categoryName = formData.get("categoryName");
    let unit = formData.get("unit");
    let count = 0

    fetch("/api/product", {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({serialNum, name, description, categoryName, unit, count})
    })
      .then(data => data.json())
      .then((data: Res<Product[]>) => {
        if (data.successCode === 'success') {
          setProducts(data.body)
          modal.onClose()
        } else {
          alert(data.msg)
        }
      })
  }

  return (
    <>
      <Button size={"sm"} radius={"full"} onPress={modal.onOpen} color="default" endContent={<PlusFilledIcon/>}>
        添加产品
      </Button>
      <Modal isOpen={modal.isOpen} onOpenChange={modal.onOpenChange}>
        <ModalContent>
          {(onClose) => (
            <>
              <form onSubmit={addProductHandler}>
                <ModalHeader className="flex flex-col gap-1">添加产品</ModalHeader>
                <ModalBody>
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
                    allowsCustomValue={true}
                    type="text"
                    placeholder={"请选择或输入产品分类"}
                    label={"产品分类"}
                    name={"categoryName"}
                    required={true}>
                    {categories.map((item) => (
                      <SelectItem key={item.id}>{item.name}</SelectItem>
                    ))}
                  </Autocomplete>
                  <Input label={"产品单位"}
                         name={"unit"}
                         required={true}
                         placeholder={"请输入产品单位"}
                         defaultValue={"个"}>
                  </Input>
                </ModalBody>
                <ModalFooter>
                  <Button color="default" variant="light" onPress={onClose}>
                    取消
                  </Button>
                  <Button type="submit" color="default" onPress={onClose}>
                    添加
                  </Button>
                </ModalFooter>
              </form>
            </>
          )}
        </ModalContent>
      </Modal>
    </>
  )
}
