<?hh // strict

// Class to store the lowest peak height in a given path of increasing mountain
// elevations as well as the actually peak heights within the path. The lowest 
// peak height is always the first peak in the path.
class PathData {
    private int $lowest_peak;
    private Vector<int> $path;

    public function __construct(int $lowest_peak, Vector<int> $path) {
        $this->lowest_peak = $lowest_peak;
        $this->path = $path;
    }

    // Return the lowest peak height (first peak in the path)
    public function getLowestPeak(): int {
        return $this->lowest_peak;
    }

    // Return the vec of peak heights within the path
    public function getPath(): Vector<int> {
        return $this->path;
    }

    public function __toString(): string {
        $string = "{lowest_peak: " . $this->lowest_peak . ", path: ";
        foreach ($this->path as $height) {
            $string = $string . $height . " ";
        }
        $string = $string . "}";
        return $string;
    }
}

function trainingForSummitingEverest(string $input): string {
    $heights = new Vector(array_map(
        $string_height ==> (int)$string_height,
        explode(" ", $input)));
    $length = count($heights);

    $routes = new Vector(null);

    // Initialize routes as an array of dicts. The dicts use a mapping of
    // {length} -> ({lowest peak height}, [peak heights in path]).
    for ($i = 0; $i < count($heights); $i++) {
        $routes[] = new Map(null);
    }

    // Set the last element in the array to contain the mapping of
    // {1} -> ({current height}, {current height}) and {0} -> (MAX_INT, []).
    $routes[$length - 1][1] = 
        new PathData($heights[$length - 1], new Vector(array($heights[$length - 1])));
    $routes[$length - 1][0] = new PathData(PHP_INT_MAX, new Vector(null));

    // Iterating the vec from the back, iterate through the keys in order of
    // descending path length. 
    for ($i = $length - 1; $i > 0; $i--) {
        foreach ($routes[$i] as $key => $val) {
            // For each of the key-value pairs, check if the peak height at 
            // index [$i - 1] is less than the lowest peak height (the value in
            // the mapping).
            $current_height = $heights[$i - 1];
            $new_length = (int)($key + 1);
            if ($current_height < $val->getLowestPeak()) {
                // If so, a new valid path can be created by adding the peak at
                // {$i - 1} to the existing path. Check if there is an existing
                // path of the same length and whether this path is better. If 
                // it is (by having a higher current height than the lowest
                // peak height), then add this to the dict at index {$i -1}.
                if (!$routes[$i - 1]->containsKey($new_length)
                    || $routes[$i - 1][$new_length]->getLowestPeak() < $current_height) {
                    $routes[$i - 1][$new_length] = 
                        new PathData($current_height, (new Vector($val->getPath()))->add($current_height));
                }
            }
            // Another valid path is the same path without using the peak at 
            // {$i - 1}, so this path is also added.
            if (!$routes[$i - 1]->containsKey($key)
                || $routes[$i - 1][$key]->getLowestPeak() < $val->getLowestPeak()) {
                $routes[$i - 1][$key] = new PathData($val->getLowestPeak(), new Vector($val->getPath()));
            }
        }
    }

    $reversed_path = $routes[0]->firstValue()?->getPath();
    $reversed_path?->reverse();
    if ($reversed_path === null) {
        return "";
    }
    $reversed_path = new Vector($reversed_path);

    $string_path = "";
    foreach ($reversed_path as $peak) {
        $string_path = $string_path . " " . $peak;
    }

    return $string_path;
}

