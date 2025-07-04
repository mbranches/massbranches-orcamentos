function Th({width, children}) {
    return (
        <th className={`${width ? width : "w-auto"} text-gray-600 font-semibold text-[14px] px-5 py-3 text-left`}>
            {children}
        </th>
    );
}

export default Th;