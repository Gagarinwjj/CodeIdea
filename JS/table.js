//火狐中没有innerText 而是contentText
//所以自定义innerText


if (!!document.getBoxObjectFor || window.mozInnerScreenX != null) {
    HTMLElement.prototype.__defineSetter__("innerText", function (sText) {
        var parsedText = document.createTextNode(sText);
        this.innerHTML = "";
        this.appendChild(parsedText);
        return parsedText;
    });
    HTMLElement.prototype.__defineGetter__("innerText", function () {
        var r = this.ownerDocument.createRange();
        r.selectNodeContents(this);
        return r.toString();
    });
}

function isIE() { //ie?
    if (window.navigator.userAgent.toLowerCase().indexOf("msie") >= 1)
        return true;
    else
        return false;
}

//window.onload = uniteTable(content, 1);

//功能：合并表格  

//参数：tb－需要合并的表格ID  

//参数：colLength－－需要对前几列进行合并，比如，  

//想合并前两列，后面的数据列忽略合并，colLength应为2  

//缺省,即不传参数时，为undefined,表示对全部列合并  
//参数：indexs 特定排序列,不传递则为undefined

function uniteTable(tb, colLength, indexs) {

    //检查表格是否规整  

    if (!checkTable(tb)) {
        alert("表格不规范！");
        return;
    }

    var i = 0;

    var j = 0;

    var allInOne = 0;//合并后有多少行,包括0行的列名称

    var rowCount = tb.rows.length; //行数  

    var colCount = tb.rows[0].cells.length; //列数  

    var obj1 = null;

    var obj2 = null;

    //为每个单元格命名  

    for (i = 0; i < rowCount; i++) {

        for (j = 0; j < colCount; j++) {

            tb.rows[i].cells[j].id = "tb__" + i.toString() + "_" + j.toString();

        }

    }
    //alert(indexs);
    //for (index in indexs) {
    //    alert(index + "--" + indexs[index]);
    //}

    //1、检查indexs指定列并且逐行合并  
    //in是获得对象的 所有属性值，对于数组而言，属性从0开始索引
    //相当于[1,2,3]={0:"1",1:"2",2:"3"}
    //比如{fname:"John",lname:"Doe",age:25} 的属性值为 "John","Doe","25"
    for (index in indexs) {
        //alert(index);//弹出0,1,2
        //通过colLength控制合并的列数,如果不传递，则为undefined，条件均不满足，不会return，即全部 
        i = indexs[index];
        obj1 = document.getElementById("tb__0_" + i.toString());
        //alert(obj1.innerText);
        for (j = 1; j < rowCount; j++) {
            obj2 = document.getElementById("tb__" + j.toString() + "_" + i.toString());

            if (typeof obj1.innerText != 'undefined' && obj1.innerText == obj2.innerText) {

                obj1.rowSpan++;
                obj2.parentNode.removeChild(obj2);

            } else {
                //obj1去样式 忽略标题行0
                if (i > 0) {
                    //obj1.setAttribute("class", "0");
                    //obj1.setAttribute("style", "background:transparent;border:1px solid #ffffff");
                    //obj1.style = "none";
                    //obj1.style.cssText = "BORDER-BOTTOM:   #4a75ce   1px   solid";  
                }
                obj1 = document.getElementById("tb__" + j.toString() + "_" + i.toString());

            }
        }
    }
    //2、检查前colCount列并且逐行合并  
    var colorflag = 0;
    for (i = 0; i < colCount; i++) {
        //通过colLength控制合并的列数,如果不传递，则为undefined，条件均不满足，不会return，即全部
        if (i == colLength) {
            number(tb, allInOne);
            return;
        }
        obj1 = document.getElementById("tb__0_" + i.toString());
        if (i == 0)//第一列
            allInOne++;
        //弹出替换的第一个，看看隐藏的时间div标签有没有包含在innerText中，结果：
        //搜狗：兼容包含标签（OK，同IE），高速不包含标签（界面错乱，同Google）
        //火狐：包含标签（OK，自立门户）
        //Google:不包含标签(界面错乱)
        //IE：包含标签（OK）
        //这些个不兼容问题，要不就用Jquery来解决，要不就换方法innerText为支持更好的innerHTML

        //var first = document.getElementById("tb__1_" + i.toString());
        //alert(first.innerText+"<------------>"+first.innerHTML);
        for (j = 1; j < rowCount; j++) {
            obj2 = document.getElementById("tb__" + j.toString() + "_" + i.toString());

            if (typeof obj1.innerHTML != 'undefined' && obj1.innerHTML == obj2.innerHTML) {

                obj1.rowSpan++;
                obj2.parentNode.removeChild(obj2);

            } else {

                obj1 = document.getElementById("tb__" + j.toString() + "_" + i.toString());
                if (i == 0)
                    allInOne++;
                //首列数据隔行变色
                if (i == 0 && colorflag == 0) {
                    obj1.setAttribute("class", "tditemfirsttwo");
                    colorflag = 1;
                } else if (i == 0 && colorflag == 1) {
                    obj1.setAttribute("class", "tditemfirst");
                    colorflag = 0;
                }
            }
        }

    }


}
/*
行头加上序号
*/
function number(tb, rowheadnum) {
    //行列没有变化
    //alert(tb.rows.length + "行," + tb.rows[0].cells.length + "列，分组有" + rowheadnum);
    var rowCount = tb.rows.length; //行数  
    var rowHead;
    var index = 1;
    //再次遍历，找合并行头
    for (var row = 1; row < rowCount ; row++) {
        rowHead = document.getElementById("tb__" + row.toString() + "_0");
        if (rowHead) {
            //alert(timeobj.innerText);
            rowHead.innerHTML = "<span class='index'>" + index + "</span>" + rowHead.innerHTML;
            index++;
        }
    }
    //alert("共检索出"+(--index)+"天的数据！");
    //或者
    alert("共检索出" + (--rowheadnum) + "天的数据！");//不包括第0行的列名称，所以要减1
}
//功能：检查表格是否规整  

//参数：tb－－需要检查的表格ID  


function checkTable(tb) {

    if (tb.rows.length == 0) return false; //行为0

    if (tb.rows[0].cells.length == 0) return false; //列为0

    for (var i = 1; i < tb.rows.length; i++) {
        //每一行，列数相等

        if (tb.rows[0].cells.length != tb.rows[i].cells.length) {
            alert("第index=" + i + "行" + tb.rows[i].cells[0].innerText + "的列数有问题！只有" + +tb.rows[i].cells.length + "列");
            return false;
        }
    }

    return true;

}