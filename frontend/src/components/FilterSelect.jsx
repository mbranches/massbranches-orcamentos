function FilterSelect({ options, onChange, value, selected }) {
    return (
        <select
            className="w-full px-4 py-2 h-full border rounded-md border-slate-300 outline-none"
            onChange={onChange}
            defaultValue={selected ? selected.value : value}
        >
            {options.map((option) => (
                <option key={option.value} value={option.value}>
                    {option.label}
                </option>
            ))}
        </select>
    );
}

export default FilterSelect;