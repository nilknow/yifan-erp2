'use client'
import {useEffect, useState} from "react";

interface Material {
  id: string;
  serialNum: string;
  name: string;
  category: string;
  count: number;
  inventoryCountAlert: number;
}

export default function Form() {
  const [sortedMaterials, setSortedMaterials] = useState<Material[]>([]);
  useEffect(()=>{
    fetch('/api/material/list')
      .then((res)=>res.json())
      .then((data)=>{
        setSortedMaterials(data)
      })
  },[])

  const handleSort = (field:string) => {
    const sorted = [...sortedMaterials].sort((a, b) => {
      // @ts-ignore
      if (a[field] < b[field]) return -1;
      // @ts-ignore
      if (a[field] > b[field]) return 1;
      return 0;
    });

    setSortedMaterials(sorted);
  };

  return (
    <>
      <div>
        <form action="/material/list" method="get" encType="multipart/form-data">
          <input type="text" name="name" placeholder="输入物料名称"/>
          <button type="submit">搜索</button>
        </form>
      </div>
      <table id="table" className="table table-hover">
        <thead>
        <tr>
          <th className="sortable" onClick={()=>handleSort('serialNum')}>编号
            <div className="arrow-box">
              <div className="arrow-up"></div>
              <div className="arrow-between"></div>
              <div className="arrow-down"></div>
            </div>
          </th>
          <th className="sortable" onClick={()=>handleSort('name')}>物料名
            <div className="arrow-box">
              <div className="arrow-up"></div>
              <div className="arrow-between"></div>
              <div className="arrow-down"></div>
            </div>
          </th>
          <th className="sortable" onClick={()=>handleSort('category')}>分类
            <div className="arrow-box">
              <div className="arrow-up"></div>
              <div className="arrow-between"></div>
              <div className="arrow-down"></div>
            </div>
          </th>
          <th>库存数量</th>
          <th>库存预警</th>
          <th>修改</th>
        </tr>
        </thead>
        <tbody>
        {sortedMaterials.map((material) => (
          <tr key={material.serialNum}>
            <td>{material.serialNum}</td>
            <td>{material.name}</td>
            <td>{material.category}</td>
            <td>{material.count}</td>
            <td>{material.inventoryCountAlert}</td>
            {/*<td>*/}
            {/*  <button th:onclick="|window.location.href='/material/update?id=${material.id}'|">修改</button>*/}
            {/*  <button className="warn" th:materialId="${material.id}" onClick="removeOnePopup(this)">删除</button>*/}
            {/*</td>*/}
          </tr>
        ))}
        </tbody>
      </table>
      <form action="#" id="full-update-popup" className="popup hidden">
        <div id="to-add-table"></div>
        <div id="to-delete-table"></div>
        <div id="to-update-table"></div>
        <button type="submit" id="full-update-submit-btn" className="warn">Submit</button>
        <button type="button" id="full-update-cancel-btn">Cancel</button>
      </form>
      <form action="#" id="popup" className="popup hidden">
        <h2>FBI WARNING!!!</h2>
        You sure you want to clean all material data? You cannot undo it after cleaning.
        <br/>
        <button id="remove-all-btn" className="warn">确认</button>
        <button id="remove-all-cancel-btn">取消</button>
      </form>

      <form action="#" id="remove-one-popup" className="popup hidden">
        <h2>FBI WARNING!!!</h2>
        You sure you want to clean this material data? You cannot undo it after cleaning.
        <br/>
        <button id="remove-one-btn" className="warn">确认</button>
        <button id="remove-one-cancel-btn">取消</button>
      </form>
    </>
  )
}
