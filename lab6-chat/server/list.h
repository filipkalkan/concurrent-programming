//  -------------------------------------------------------------------------
//
//  list:
//
//  Functions for creating, using, and destroying lists. Inspired by
//  Java's ArrayList.
//
//  -------------------------------------------------------------------------

struct list;

/**
 * Create a new empty list.
 */
struct list *
list_create();

/**
 * Add an element to the list l (in the last position).
 */
int
list_add(struct list *l, void *data);

/**
 * Returns the element at position _index_ in the list l.
 */
void *
list_get(struct list *l, int index);

/**
 * Returns the number of elements in the list l.
 */
int
list_size(struct list *l);
