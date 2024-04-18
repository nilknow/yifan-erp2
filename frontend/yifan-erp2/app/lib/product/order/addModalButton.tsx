import {
  Autocomplete,
  Button,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader, Select,
  SelectItem, useDisclosure
} from "@nextui-org/react";
import {PlusFilledIcon} from "@nextui-org/shared-icons";
import React, {useEffect} from "react";
import {Input} from "@nextui-org/input";
import Product from "@/app/dto/product";
import Res from "@/app/dto/res";
import Category from "@/app/dto/category";
import myFetch from "@/app/myFetch";

export default function AddModalButton() {
  const modal = useDisclosure();
  const [products, setProducts] = React.useState<Product[]>([]);

  useEffect(() => {
    myFetch(`/api/product/list`)
      .then((res) => res.json())
      .then((data: Res<Product[]>) => {
        if (data.successCode === 'success') {
          setProducts(data.body);
        } else {
          alert(data.msg)
        }
      })
  }, [])

  async function addOrderHandler(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();

    const form = e.currentTarget;
    const formData = new FormData(form);
    let serialNum = formData.get("serialNum");
    let productId = formData.get("productId");
    let count = formData.get("count");
    let produceDaysTake = formData.get("produceDaysTake");
    let deliverySerialNum = formData.get("deliverySerialNum");
    let customer = formData.get("customer");
    let note = formData.get("note");

    myFetch("/api/order?source=button", {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({serialNum, productId, count, produceDaysTake, deliverySerialNum, customer, note})
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
        添加订单
      </Button>
      <Modal backdrop={"blur"} isOpen={modal.isOpen} onOpenChange={modal.onOpenChange}>
        <ModalContent>
          {(onClose) => (
            <>
              <form onSubmit={addOrderHandler}>
                <ModalHeader className="flex flex-col gap-1">添加订单</ModalHeader>
                <ModalBody>
                  <Input label={"订单编号"}
                         name={"serialNum"}
                         required={true}
                         placeholder={"请输入订单编号（必须唯一）"}>
                  </Input>
                  <Select
                    label="请选择产品"
                    name={"productId"}
                  >
                    {products.map((product:Product) => (
                      <SelectItem key={product.id} value={product.id}>
                        {product.name}
                      </SelectItem>
                    ))}
                  </Select>
                  <Input label={"数目"}
                         name={"count"}
                         type={"number"}
                         defaultValue={"1"}
                         required={true}
                         placeholder={"请输入订单描述"}>
                  </Input>
                  <Input label={"生产耗时（天）"}
                         name={"produceDaysTake"}
                         type={"number"}
                         required={false}
                         placeholder={"请输入生产耗时，单位为天"}>
                  </Input>
                  <Input label={"物流单号"}
                         name={"deliverySerialNum"}
                         required={false}
                         placeholder={"请输入物流单号"}>
                  </Input>
                  <Input label={"客户姓名"}
                         name={"customer"}
                         required={false}
                         placeholder={"请输入客户姓名"}>
                  </Input>
                  <Input label={"备注"}
                         name={"note"}
                         required={false}>
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
