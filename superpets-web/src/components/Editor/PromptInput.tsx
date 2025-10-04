interface PromptInputProps {
  value: string;
  onChange: (value: string) => void;
}

const PROMPT_SUGGESTIONS = [
  "Add sunglasses and a hat",
  "Change background to beach",
  "Make fur rainbow colored",
  "Put in a superhero costume",
  "Add party decorations",
];

export function PromptInput({ value, onChange }: PromptInputProps) {
  return (
    <div className="space-y-4">
      <div>
        <label className="block text-sm font-semibold text-gray-700 mb-2">
          Edit Prompt
        </label>
        <textarea
          value={value}
          onChange={(e) => onChange(e.target.value)}
          placeholder="Describe what you want to do with the images..."
          className="input-field min-h-[100px] resize-none"
          maxLength={500}
        />
        <div className="text-xs text-gray-500 mt-1 text-right">
          {value.length}/500 characters
        </div>
      </div>

      <div>
        <p className="text-sm font-medium text-gray-700 mb-2">Suggestions:</p>
        <div className="flex flex-wrap gap-2">
          {PROMPT_SUGGESTIONS.map((suggestion) => (
            <button
              key={suggestion}
              onClick={() => onChange(suggestion)}
              className="px-3 py-1 bg-gray-100 hover:bg-gray-200 text-gray-700 text-sm rounded-full transition-colors"
            >
              {suggestion}
            </button>
          ))}
        </div>
      </div>
    </div>
  );
}


