'use client'
import {Input, Textarea} from "@nextui-org/input";
import {Button, Table, TableBody, TableCell, TableColumn, TableHeader, TableRow} from "@nextui-org/react";
import {useEffect, useState} from "react";

interface Bug{

}

export default function Page() {
  const [sortedBugs, setSortedBugs] = useState<Material[]>([]);
  useEffect(() => {
    fetch('/api/bug/list')
      .then((res) => res.json())
      .then((data) => {
        setSortedBugs(data)
      })
  }, [])

  return (
    <div className={"mx-4"}>
      <form className={"min-w-96 w-1/2"}>
        <div className={"flex gap-4"}>
          <Input type="text" size={"sm"} label={"email"} name={"email"} placeholder={"你的电子邮箱（选填）"}></Input>
          <Input type="text" size={"sm"} label={"phone"} name={"phone"} placeholder={"你的手机号（选填）"}></Input>
          <input type="hidden" required name="priority" value="一般"/>
        </div>
        <Textarea
          className={"mt-2"}
          minRows={10}
          label={"bug details"}
          name="description"
          required
          placeholder={"bug详情描述（必填）"}
        />
        <div className={"mt-2 flex gap-4"}>
          <Button type={"submit"}>提交</Button>
          <Button type={"reset"}>重填</Button>
        </div>
      </form>
      <div className={"mt-4"}>
        <Table>
          <TableHeader>
            <TableColumn>time</TableColumn>
          </TableHeader>
          <TableBody>
            {sortedBugs.map((bug) => (
              <TableRow key={bug.id}>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>
    </div>
  )
}
