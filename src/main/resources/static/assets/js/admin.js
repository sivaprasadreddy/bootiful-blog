
function toSlug(str) {
    str = str.replace(/^\s+|\s+$/g, ''); // trim
    str = str.toLowerCase();
    str = str.replace(/[^a-z0-9 -]/g, '') // remove invalid chars
        .replace(/\s+/g, '-') // collapse whitespace and replace by -
        .replace(/-+/g, '-'); // collapse dashes

    return str;
}

function fillSlugFromText(srcEleId, targetEleId) {
  const text = document.getElementById(srcEleId).value;
  document.getElementById(targetEleId).value = toSlug(text);
}