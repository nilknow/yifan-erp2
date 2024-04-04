import {
  Autocomplete, AutocompleteItem,
  Button,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
  Select,
  SelectItem,
  useDisclosure
} from "@nextui-org/react";
import {PlusFilledIcon} from "@nextui-org/shared-icons";
import {Input} from "@nextui-org/input";
import React, {useEffect} from "react";
import Material from "@/app/dto/material";
import Res from "@/app/dto/res";

export default function AddModalButton() {
  const modal = useDisclosure();
  const [materialTypes, setMaterialTypes] = React.useState<string[]>([]);
  const [selectedMaterialType, setSelectedMaterialType] = React.useState<string>("");

  useEffect(() => {
    fetch('/api/material/list')
      .then((res) => res.json())
      .then((data: Res<Material[]>) => {
        if ("success" === data.successCode) {
          setMaterialTypes(Array.from(new Set(data.body.map(material => material.category))))
        } else {
          alert(data.msg)
        }
      })
  }, [])

  async function addMaterialHandler(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault()
    const form = e.currentTarget;
    const formData = new FormData(form);
    let serialNum = formData.get("serialNum");
    let name = formData.get("name");
    let category = formData.get("category");
    let count = formData.get("count");
    let inventoryCountAlert = formData.get("inventoryCountAlert");

    fetch('/api/material', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          serialNum,
          name,
          category,
          count,
          inventoryCountAlert
        })
      }
    ).then((res) => res.json())
      .then((data) => {
        if ("success" === data["successCode"]) {
          modal.onClose();
          window.location.reload();
        } else {
          alert(data["msg"])
        }
      })
  }

  return (
    <>
      <Button size={"sm"} radius={"full"} onPress={modal.onOpen} color="default" endContent={<PlusFilledIcon/>}>
        添加物料
      </Button>
      <Modal isOpen={modal.isOpen} onOpenChange={modal.onOpenChange}>
        <ModalContent>
          {(onClose) => (
            <>
              <form onSubmit={addMaterialHandler}>
                <ModalHeader className="flex flex-col gap-1">添加物料</ModalHeader>
                <ModalBody>
                  <Input
                    label={"物料编号"}
                    name={"serialNum"}
                    required={true}
                    placeholder={"请输入物料名称（必须唯一）"}>
                  </Input>
                  <Input
                    label={"物料名称"}
                    name={"name"}
                    required={true}
                    placeholder={"请输入物料名称"}>
                  </Input>
                  <Autocomplete
                    allowsCustomValue={true}
                    name={"category"}
                    label="选择分类"
                    placeholder={"请选择或输入物料分类名称"}
                  >
                    {materialTypes.map((item, index) => (
                      <SelectItem key={index}>{item}</SelectItem>
                    ))}
                  </Autocomplete>
                  <Input
                    label={"库存数量"}
                    name={"count"}
                    required={true}
                    placeholder={"请输入库存数量"}
                    defaultValue={"1"}
                    type={"number"}>
                  </Input>
                  <Input
                    label={"库存预警阈值"}
                    name={"inventoryCountAlert"}
                    required={true}
                    placeholder={"请输入库存预警阈值"}
                    defaultValue={"-1"}
                    type={"number"}>
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
