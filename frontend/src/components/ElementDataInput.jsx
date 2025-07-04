function ElementDataInput({ value, onChange, error, placeholder, onBlur, onFocus, disabled, throwableError }) {
    return (
        <div>
            <input
                type="text"
                value={value}
                onChange={onChange}
                className={`border border-gray-300 rounded px-2 py-1 w-full outline-none ${error ? "border-red-600" : "border-gray-300"}`}
                placeholder={placeholder}
                onBlur={onBlur}
                onFocus={onFocus}
                disabled={disabled}
            />  

            {throwableError && <p className='text-red-600 text-xs font-normal'>{throwableError}</p>}
        </div>
    );
}

export default ElementDataInput;